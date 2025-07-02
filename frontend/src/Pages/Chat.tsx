import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { Button, TextInput, ScrollArea, Text, Group } from '@mantine/core';
import { connectWebSocket, sendMessage, getChatHistory, disconnectWebSocket } from '../Services/ChatService';
import { timeAgo } from '../Services/Utilities';

interface ChatProps {
    recipientId: number;
}

const Chat: React.FC<ChatProps> = ({ recipientId }) => {
    const user = useSelector((state: any) => state.user);
    const [messages, setMessages] = useState<any[]>([]);
    const [messageInput, setMessageInput] = useState<string>('');

    useEffect(() => {
        if (!user.id || !recipientId) {
            console.error('Invalid user or recipient ID:', user.id, recipientId);
            return;
        }

        // Fetch chat history
        getChatHistory(user.id, recipientId)
            .then((history) => {
                console.log('Loaded Chat History:', history);
                setMessages(history);
            })
            .catch((error) => {
                console.error('Failed to load chat history:', error);
            });

        // Connect to WebSocket
        connectWebSocket(user.id, recipientId, (message: any) => {
            console.log('Updating Messages with:', message);
            setMessages((prevMessages) => [...prevMessages, message]);
        });

        // Cleanup on unmount
        return () => {
            disconnectWebSocket();
        };
    }, [user.id, recipientId]);

    const handleSendMessage = () => {
        if (messageInput.trim()) {
            console.log('Sending Message:', messageInput);
            const newMessage = {
                senderId: user.id,
                recipientId,
                content: messageInput,
                type: 'CHAT',
                timestamp: new Date().toISOString(),
            };
            setMessages((prevMessages) => [...prevMessages, newMessage]);
            sendMessage(user.id, recipientId, messageInput);
            setMessageInput('');
        }
    };

    return (
        <div className="p-4 bg-gray-100 rounded-lg shadow-md">
            <ScrollArea style={{ height: '400px' }} className="border p-4 bg-white rounded-md">
                {messages.map((msg, index) => (
                    <div
                        key={index}
                        className={`mb-2 p-2 rounded-lg ${
                            msg.senderId === user.id ? 'bg-blue-100 ml-auto' : 'bg-gray-200'
                        } max-w-xs`}
                    >
                        <Text size="sm">
                            {msg.senderId === user.id ? 'You' : 'Other'}
                        </Text>
                        <Text size="sm">{msg.content}</Text>
                        <Text size="xs" color="dimmed">
                            {timeAgo(msg.timestamp)}
                        </Text>
                    </div>
                ))}
            </ScrollArea>
            <Group className="mt-4">
                <TextInput
                    placeholder="Type your message..."
                    value={messageInput}
                    onChange={(e) => setMessageInput(e.currentTarget.value)}
                    className="flex-grow"
                />
                <Button onClick={handleSendMessage} color="teal" disabled={!messageInput.trim()}>
                    Send
                </Button>
            </Group>
        </div>
    );
};

export default Chat;