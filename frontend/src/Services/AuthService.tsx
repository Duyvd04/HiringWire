import axios from 'axios';
const base_url = "http://localhost:8080/auth/"
// const base_url = "https://1773-2402-800-7ca0-4871-95b9-f07b-7087-7f3.ngrok-free.app/auth/"
const loginUser = async (login:any)=> {
    return axios.post(`${base_url}login`, login)
        .then((result:any) => result.data)
        .catch((error:any) =>{throw error;});
}

export {loginUser};