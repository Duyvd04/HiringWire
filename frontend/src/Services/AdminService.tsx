// src/Services/AdminService.ts
import axiosInstance from '../Interceptor/AxiosInterceptor';

export const getAllUsers = async () => {
    try {
        const response = await axiosInstance.get('/admin/users');
        return response.data;
    } catch (error) {
        console.error('Error fetching users:', error);
        throw error;
    }
};

export const getAllJobs = async () => {
    try {
        const response = await axiosInstance.get('/admin/jobs');
        return response.data;
    } catch (error) {
        console.error('Error fetching jobs:', error);
        throw error;
    }
};

export const changeAccountStatus = async (id: number, status: string) => {
    try {
        const response = await axiosInstance.post(`/admin/users/${id}/status/${status}`);
        return response.data;
    } catch (error) {
        console.error('Error changing account status:', error);
        throw error;
    }
};

export const getPendingEmployers = async () => {
    try {
        const response = await axiosInstance.get('/admin/employers/pending');
        return response.data;
    } catch (error) {
        console.error('Error fetching pending employers:', error);
        throw error;
    }
};

export const approveEmployer = async (id: number) => {
    try {
        const response = await axiosInstance.post(`/admin/employers/${id}/approve`);
        return response.data;
    } catch (error) {
        console.error('Error approving employer:', error);
        throw error;
    }
};

export const rejectEmployer = async (id: number) => {
    try {
        const response = await axiosInstance.post(`/admin/employers/${id}/reject`);
        return response.data;
    } catch (error) {
        console.error('Error rejecting employer:', error);
        throw error;
    }
};