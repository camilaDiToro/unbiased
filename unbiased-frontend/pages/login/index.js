import Link from "next/link";
import {useAppContext} from "../../context";
import {useEffect, useState} from "react";
import axios from "axios";
import baseURL from "../back";
import {useRouter} from "next/router";


export default function Login() {

    const ctx = useAppContext()
    const router = useRouter()
    const [jwt, setJwt] = ctx.jwtState
    const [details, setDetails] = useState({
        username: "",
        password: "",
        rememberMe: false
    })
    useEffect(() => {
        if (router.pathname !== '/login')
            localStorage.removeItem('fromPage')

    }, [router.pathname])
    const [passwordType, setPasswordType] = useState("password");
    const handleChange = (e) => {

        const {name, value} = e.target
        if (name !== "rememberMe") {
            setDetails((prev) => {
                return {...prev, [name]: value}
            })
        } else {
            setDetails((prev) => {
                return {...prev, [name]: !details.rememberMe}
            })
        }

    }

    const togglePassword = () => {
        if (passwordType === "password") {
            setPasswordType("text")
            return;
        }
        setPasswordType("password")
    }

    async function handleSubmit(e) {
        e.preventDefault();
        ctx.axios({
            // headers: {'Content-Type': 'application/json'},
            method: 'put',
            url: 'users/13/pingNews/4',
            auth: {
                username: 'kevincatino18@gmail.com',
                password: '123'
            }
        }).then(res => {
            console.log(res.data)
            console.log(res.headers)
            // router.back()
        }).catch(error => {
            console.log(error.response.headers)
            if (error.response.status === 404) {

                if (localStorage.getItem('fromPage')) {
                    router.back();
                } else {
                    localStorage.removeItem('fromPage')
                    router.push('/')
                }
            }
        })
    }

    return(
        <div className="d-flex flex-column justify-content-center align-items-center height-100vh">

            <div className="form-signIn d-flex flex-column align-items-center " onSubmit={(e)=>{handleSubmit(e)}}>

                <h1 id="title-log" className="logo mb-5">
                    <Link className="text-info link" href="/">
                        unbiased
                    </Link>
                </h1>

                <div className="d-flex mb-4">
                    <img className="size-img-modal-login align-self-center" src="/img/profile-svgrepo-com.svg" alt="..."/>
                    <input onChange={handleChange} type="text" id="username" name="username" placeholder="EmailAddress" className="sign-form-control" required="" autoFocus=""/>
                </div>

                <div className="mt-1 d-flex flex-row justify-content-center align-items-center position-relative">
                    <img src="/img/lock-svgrepo-com.svg" alt="..." className="size-img-modal-login align-self-center"/>
                    <input type={passwordType} onChange={handleChange} name="password" placeholder="Password" className="sign-form-control h-fit mb-1"/>
                    <button className="btn  eye-button-properties" onClick={togglePassword}>
                        { passwordType==="password"? <img src="/img/eye.svg"/> : <img src="/img/eye-slash.svg"/> }
                    </button>
                </div>

                <div className="checkbox mb-3">
                    <label className="text-light">
                        <input onChange={handleChange} className="mr-1" type="checkbox" name="rememberMe"/>
                        Remember me
                    </label>
                </div>

                <button onClick={handleSubmit} type="submit" className="btn btn-md btn-info btn-block">Log in</button>
                <p className="mt-5 mb-3 text-muted">© 2022-2022</p>
            </div>
        </div>
    )
}