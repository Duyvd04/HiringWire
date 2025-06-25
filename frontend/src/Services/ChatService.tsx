import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import axiosInstance from '../Interceptor/AxiosInterceptor';
import { successNotification, errorNotification } from './NotificationService';

let stompClient: Client | null = null;
let reconnectAttempts = 0;
const maxReconnectAttempts = 3;
const reconnectDelay = 2000;

export const connectWebSocket = (
    userId: number,
    recipientId: number,
    onMessageReceived: (message: any) => void
) => {
    if (stompClient && stompClient.connected) {
        console.log('WebSocket already connected for User:', userId);
        return;
    }

    const token = localStorage.getItem('token');
    if (!token) {
        console.error('No JWT token found in localStorage');
        errorNotification('Error', 'Please log in to use chat.');
        return;
    }

    const socket = new SockJS('http://localhost:8080/chat');
    stompClient = new Client({
        webSocketFactory: () => socket,
        connectHeaders: { Authorization: `Bearer ${token}` },
        debug: (str) => console.log('STOMP Debug:', str),
        onConnect: () => {
            console.log('WebSocket Connected for User:', userId, 'Recipient:', recipientId);
            reconnectAttempts = 0;
            stompClient?.subscribe(`/topic/chat/${userId}/${recipientId}`, (message) => {
                console.log('Message Received:', message.body);
                const receivedMessage = JSON.parse(message.body);
                onMessageReceived(receivedMessage);
            });
            stompClient?.subscribe(`/topic/chat/${recipientId}/${userId}`, (message) => {
                console.log('Message Received (Reverse):', message.body);
                const receivedMessage = JSON.parse(message.body);
                onMessageReceived(receivedMessage);
            });
            successNotification('Chat Connected', 'You are now connected to the chat.');
        },
        onStompError: (frame) => {
            console.error('STOMP Error:', frame.headers['message'], frame.body);
            errorNotification('Chat Error', `STOMP Error: ${frame.body}`);
            attemptReconnect(userId, recipientId, onMessageReceived);
        },
        onWebSocketError: (error) => {
            console.error('WebSocket Error:', error);
            errorNotification('WebSocket Error', 'Failed to establish WebSocket connection.');
            attemptReconnect(userId, recipientId, onMessageReceived);
        },
        onWebSocketClose: (event) => {
            console.log('WebSocket Closed:', event.code, event.reason);
            errorNotification('Chat Disconnected', 'WebSocket connection closed.');
            attemptReconnect(userId, recipientId, onMessageReceived);
        },
    });

    console.log('Activating WebSocket with headers:', { Authorization: `Bearer ${token}` });
    stompClient.activate();
};

const attemptReconnect = (
    userId: number,
    recipientId: number,
    onMessageReceived: (message: any) => void
) => {
    if (reconnectAttempts < maxReconnectAttempts) {
        reconnectAttempts++;
        console.log(`Reconnecting WebSocket (${reconnectAttempts}/${maxReconnectAttempts})...`);
        setTimeout(() => connectWebSocket(userId, recipientId, onMessageReceived), reconnectDelay);
    } else {
        console.error('Max reconnect attempts reached');
        errorNotification('Error', 'Failed to reconnect to chat server.');
    }
};

export const sendMessage = (senderId: number, recipientId: number, content: string) => {
    if (stompClient && stompClient.connected) {
        const chatMessage = {
            senderId,
            recipientId,
            content,
            type: 'CHAT',
            timestamp: new Date().toISOString(),
        };
        console.log('Sending Message:', chatMessage);
        stompClient.publish({
            destination: '/app/chat.sendMessage',
            body: JSON.stringify(chatMessage),
        });
    } else {
        console.error('Cannot send message: STOMP client not connected');
        errorNotification('Error', 'Cannot send message: Not connected to chat server.');
    }
};

export const getChatHistory = async (userId: number, recipientId: number) => {
    try {
        const response = await axiosInstance.get(`/chat/history/${userId}/${recipientId}`);
        console.log('Chat History:', response.data);
        return response.data;
    } catch (error: any) {
        console.error('getChatHistory Error:', error.message, error.config);
        errorNotification('Error', 'Failed to fetch chat history. Please try again.');
        throw error;
    }
};

export const getUsersForChat = async (userId: number, accountType: string) => {
    try {
        const response = await axiosInstance.get(`/users/chat/users/${userId}/${accountType}`);
        return response.data;
    } catch (error: any) {
        console.error('getUsersForChat Error:', error.message, error.config);
        errorNotification('Error', 'Failed to fetch users for chat.');
        throw error;
    }
};

export const disconnectWebSocket = () => {
    if (stompClient) {
        stompClient.deactivate();
        console.log('WebSocket Disconnected');
        successNotification('Chat Disconnected', 'You have been disconnected from the chat.');
        stompClient = null;
        reconnectAttempts = 0;
    }
};