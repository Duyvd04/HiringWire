import axios from 'axios';
const base_url = "https://hiringwire-production.up.railway.app/auth/"
const loginUser = async (login:any)=> {
    return axios.post(`${base_url}login`, login)
        .then((result:any) => result.data)
        .catch((error:any) =>{throw error;});
}

export {loginUser};