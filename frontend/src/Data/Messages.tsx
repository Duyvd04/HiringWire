import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { Group, Text, ScrollArea, Divider } from '@mantine/core';
import { getUsersForChat } from '../Services/ChatService';
import { errorNotification } from '../Services/NotificationService';
import Chat from '../Pages/Chat';

interface User {
    id: number;
    name: string;
    accountType: string;
}

const Messages: React.FC = () => {
    const user = useSelector((state: any) => state.user);
    const [users, setUsers] = useState<User[]>([]);
    const [selectedUserId, setSelectedUserId] = useState<number | null>(null);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const data = await getUsersForChat(user.id, user.accountType);
                setUsers(data);
            } catch (error) {
                errorNotification('Error', 'Failed to fetch users for chat.');
            }
        };

        fetchUsers();
    }, [user.id, user.accountType]);

    const handleSelectUser = (userId: number) => {
        setSelectedUserId(userId);
    };

    return (
        <div className="p-4">
            <Group className="mb-4">
                <Text size="lg">Conversations</Text>
            </Group>
            <ScrollArea style={{ height: '400px' }} className="border p-4 bg-white rounded-md">
                {users.length === 0 ? (
                    <Text color="dimmed">No users available to chat.</Text>
                ) : (
                    users.map((u) => (
                        <div
                            key={u.id}
                            className={`p-2 mb-2 rounded-lg cursor-pointer hover:bg-gray-100 ${
                                selectedUserId === u.id ? 'bg-blue-100' : ''
                            }`}
                            onClick={() => handleSelectUser(u.id)}
                        >
                            <Text>{u.name}</Text>
                            <Text size="sm" color="dimmed">{u.accountType}</Text>
                        </div>
                    ))
                )}
            </ScrollArea>
            {selectedUserId && (
                <>
                    <Divider my="sm" />
                    <Chat recipientId={selectedUserId} />
                </>
            )}
        </div>
    );
};

export default Messages;