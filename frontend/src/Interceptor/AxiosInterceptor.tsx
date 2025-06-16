import axios, { InternalAxiosRequestConfig } from "axios";
import { removeUser } from "../Slices/UserSlice";
import { removeJwt } from "../Slices/JwtSlice";

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080'
    // baseURL: 'https://1773-2402-800-7ca0-4871-95b9-f07b-7087-7f3.ngrok-free.app'
    // baseURL: 'https://hiringwire-production.up.railway.app'
});

axiosInstance.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
)

export const setupResponseInterceptor = (navigate: any, dispatch: any) => {
    axiosInstance.interceptors.response.use(
        (response) => {
            return response;
        },
        (error) => {
            if (error.response?.status === 401) {

                dispatch(removeUser());
                dispatch(removeJwt());
                navigate('/login');
            }
            return Promise.reject(error);
        }
    )
}

export default axiosInstance;
