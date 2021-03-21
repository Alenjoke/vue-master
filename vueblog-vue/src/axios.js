import axios from 'axios'
import ElementUI from 'element-ui'
import store from './store'
import router from './router'

axios.defaults.baseURL="http://localhost:8081"

//前置拦截
axios.interceptors.request.use(config => {

    return config
})

//后置拦截
axios.interceptors.response.use(response => {
    let res = response.data;

    console.log("=========================")
    console.log(res)
    console.log("=========================")

    if (res.code === 200) {
        return response
    } else {
        ElementUI.Message.error({message: res.msg, duration: 2000});

        //不执行后续的操作
        return Promise.reject(response.data.msg)
    }

}, error => {
    console.log(error.response)
    if(error.response.status === 401) {
        
        store.commit("REMOVE_INFO")
        router.push("/login")
    } 

    let msg = error.response.data.data;
    
    if (msg == null) {
        msg = error.response.data.msg;
    }
    ElementUI.Message.error({message: msg, duration: 2000});
    

    return Promise.reject(error.response.data.msg)

})