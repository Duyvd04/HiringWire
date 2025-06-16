import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import axios from 'axios';
import { Button, Select, Textarea, Title, Paper, Grid, Text, Notification, Tooltip } from '@mantine/core';
import { showOverlay, hideOverlay } from '../Slices/OverlaySlice';

// Define the expected shape of the user state (from userSlice)
interface User {
    id: number;
    accountType: string;
    // Add other user properties as needed
}

// Define the root state for Redux
interface RootState {
    user: User | null;
    jwt: string;
    overlay: boolean;
    filter: any;
    profile: any;
    sort: any;
}

// Define the CvData interface to match the backend Cv entity
interface CvData {
    id?: number;
    userId: number;
    personalInfo: string;
    education: string;
    experience: string;
    skills: string;
}

// Define template interface for clarity
interface Template {
    id: string;
    name: string;
    style: string;
}

const templates: Template[] = [
    { id: 'template1', name: 'Classic Template', style: 'p-6 shadow-md bg-white' },
    { id: 'template2', name: 'Modern Template', style: 'p-6 shadow-lg bg-gray-50 border border-gray-200' },
];

// Base URL from environment variable
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

const CvEditor: React.FC = () => {
    const dispatch = useDispatch();
    const user = useSelector((state: RootState) => state.user);
    const token = useSelector((state: RootState) => state.jwt);
    const [cv, setCv] = useState<CvData>({
        userId: user?.id || 0,
        personalInfo: '',
        education: '',
        experience: '',
        skills: '',
    });
    const [selectedTemplate, setSelectedTemplate] = useState<Template>(templates[0]);
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(false);

    // Validate user ID on component mount and when user changes
    useEffect(() => {
        if (!user?.id) {
            setError('You must be logged in to create a CV.');
        } else {
            setCv((prev) => ({ ...prev, userId: user.id }));
            setError(null);
        }
    }, [user]);

    const handleChange = (name: keyof CvData, value: string) => {
        setCv({ ...cv, [name]: value });
        setError(null);
    };

    const saveCv = async () => {
        if (!user?.id) {
            setError('You must be logged in to save a CV.');
            return;
        }

        setIsLoading(true);
        dispatch(showOverlay());
        try {
            const response = await axios.post(`${API_BASE_URL}/api/cv`, {
                ...cv,
                userId: user.id,
            }, {
                headers: token ? { Authorization: `Bearer ${token}` } : {},
            });
            setCv({ ...cv, id: response.data.id });
            setError(null);
            alert('CV saved successfully!');
        } catch (error: any) {
            console.error('Error saving CV:', error);
            const message = error.response?.status === 401 ? 'Unauthorized: Please log in again.' :
                error.response?.data?.message ||
                (error.code === 'ERR_NETWORK' ? 'Network error: Unable to connect to the server.' : 'Failed to save CV. Please try again.');
            setError(message);
        } finally {
            setIsLoading(false);
            dispatch(hideOverlay());
        }
    };

    const downloadPdf = async () => {
        if (!cv.id) {
            setError('Please save the CV before downloading.');
            return;
        }

        if (!user?.id) {
            setError('You must be logged in to download a CV.');
            return;
        }

        setIsLoading(true);
        dispatch(showOverlay());
        try {
            const response = await axios.get(`${API_BASE_URL}/api/cv/${cv.id}/pdf`, {
                responseType: 'blob',
                headers: token ? { Authorization: `Bearer ${token}` } : {},
            });
            const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `cv-${cv.id}.pdf`);
            document.body.appendChild(link);
            link.click();
            link.remove();
            window.URL.revokeObjectURL(url);
            setError(null);
        } catch (error: any) {
            console.error('Error downloading PDF:', error);
            const message = error.response?.status === 401 ? 'Unauthorized: Please log in again.' :
                error.response?.data?.message ||
                (error.code === 'ERR_NETWORK' ? 'Network error: Unable to connect to the server.' : 'Failed to download PDF. Please try again.');
            setError(message);
        } finally {
            setIsLoading(false);
            dispatch(hideOverlay());
        }
    };

    return (
        <div className="container mx-auto p-4">
            <Title order={1} mb="lg">CV Editor</Title>

            {error && (
                <Notification color="red" title="Error" onClose={() => setError(null)} mb="md">
                    {error}
                </Notification>
            )}

            <Select
                label="Select Template"
                value={selectedTemplate.id}
                onChange={(value) => {
                    const template = templates.find((t) => t.id === value);
                    if (template) {
                        setSelectedTemplate(template);
                    }
                }}
                data={templates.map((template) => ({ value: template.id, label: template.name }))}
                mb="md"
                disabled={isLoading}
            />

            <Grid>
                <Grid.Col span={6}>
                    <Title order={2} mb="md">Edit CV</Title>
                    <Textarea
                        label="Personal Info"
                        value={cv.personalInfo}
                        onChange={(e) => handleChange('personalInfo', e.currentTarget.value)}
                        minRows={4}
                        mb="md"
                        disabled={isLoading}
                        placeholder="Enter your personal information..."
                    />
                    <Textarea
                        label="Education"
                        value={cv.education}
                        onChange={(e) => handleChange('education', e.currentTarget.value)}
                        minRows={4}
                        mb="md"
                        disabled={isLoading}
                        placeholder="Enter your education details..."
                    />
                    <Textarea
                        label="Experience"
                        value={cv.experience}
                        onChange={(e) => handleChange('experience', e.currentTarget.value)}
                        minRows={4}
                        mb="md"
                        disabled={isLoading}
                        placeholder="Enter your work experience..."
                    />
                    <Textarea
                        label="Skills"
                        value={cv.skills}
                        onChange={(e) => handleChange('skills', e.currentTarget.value)}
                        minRows={4}
                        mb="md"
                        disabled={isLoading}
                        placeholder="Enter your skills..."
                    />
                    <Button onClick={saveCv} color="blue" mr="sm" loading={isLoading} disabled={!user?.id}>
                        Save CV
                    </Button>
                    <Tooltip label={cv.id ? 'Download your CV as a PDF' : 'Please save the CV first'} position="top">
                        <Button onClick={downloadPdf} color="green" loading={isLoading} disabled={!cv.id || !user?.id}>
                            Download PDF
                        </Button>
                    </Tooltip>
                </Grid.Col>
                <Grid.Col span={6}>
                    <Paper className={selectedTemplate.style} p="md">
                        <Title order={2} mb="md">Preview</Title>
                        <div className="mb-4">
                            <Text fw={700} size="lg">Personal Info</Text>
                            <Text className="whitespace-pre-wrap">{cv.personalInfo || 'Enter your personal info...'}</Text>
                        </div>
                        <div className="mb-4">
                            <Text fw={700} size="lg">Education</Text>
                            <Text className="whitespace-pre-wrap">{cv.education || 'Enter your education...'}</Text>
                        </div>
                        <div className="mb-4">
                            <Text fw={700} size="lg">Experience</Text>
                            <Text className="whitespace-pre-wrap">{cv.experience || 'Enter your experience...'}</Text>
                        </div>
                        <div className="mb-4">
                            <Text fw={700} size="lg">Skills</Text>
                            <Text className="whitespace-pre-wrap">{cv.skills || 'Enter your skills...'}</Text>
                        </div>
                    </Paper>
                </Grid.Col>
            </Grid>
        </div>
    );
};

export default CvEditor;