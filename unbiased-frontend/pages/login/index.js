import Link from "next/link";
import {useAppContext} from "../../context";
import {useEffect, useState} from "react";
import {useRouter} from "next/router";
import {getResourcePath} from "../../constants";


export default function Login() {

    const {I18n, api} = useAppContext()
    const router = useRouter()
    const [details, setDetails] = useState({
        username: undefined,
        password: "",
    })

    const [passwordType, setPasswordType] = useState("password");
    const handleChange = (e) => {

        const {name, value} = e.target
        if (name !== "rememberMe") {
            setDetails((prev) => {
                return {...prev, [name]: value}
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

    const validEmail = () => {
        const regex = /^[\w!#$%&'*+/=?`{|}~^-]+(?:\.[\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,6}$/

        return details.username && regex.test(details.username)
    }
    const validForm = () => {
        return validEmail() && details.password
    }

    async function handleSubmit(e) {
        e.preventDefault();
        if (!validForm())
            return
        api.login(details.username, details.password).then(r => {
            const {success} = r
            if (success) {
                router.push('/')
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

                <div className={`d-flex ${details.username === undefined || validEmail() ? 'mb-4' : 'mb-1'}`}>
                    <img className="size-img-modal-login align-self-center" src={getResourcePath("/img/profile-svgrepo-com.svg")} alt="..."/>
                    <input onChange={handleChange} type="text" title="username" id="username" name="username" placeholder="EmailAddress" className="sign-form-control" required="" autoFocus=""/>
                </div>
                {details.username === undefined || validEmail() ? <></> : <div className="text-danger mb-4">{I18n("register.invalidEmail")}</div>}


                <div className=" mb-2 mt-1 d-flex flex-row justify-content-center align-items-center position-relative">
                    <img src={getResourcePath("/img/lock-svgrepo-com.svg")} alt="..." className="size-img-modal-login align-self-center"/>
                    <input type={passwordType} onChange={handleChange} data-testid="password" name="password" placeholder="Password" className="sign-form-control h-fit mb-1"/>
                    <button className="btn  eye-button-properties" onClick={togglePassword}>
                        { passwordType==="password"? <img alt="eye" src={getResourcePath("/img/eye.svg")}/> : <img alt="eyeSlash" src={getResourcePath("/img/eye-slash.svg")}/> }
                    </button>
                </div>



                <button onClick={handleSubmit} type="submit" className={`btn btn-md btn-info btn-block ${validForm() ? '' : 'disabled noHover'}`}>{I18n("navbar.logIn")}</button>
                <div className="d-flex mt-2">
                    <div className="mr-2">
                        {I18n("login.notMemberYet")}
                    </div>
                    <Link href={`/register`} className="link text-underline blue-text">{I18n("navbar.register")}</Link>
                </div>


                <p className="mt-5 mb-3 text-muted">© 2022-2022</p>
            </div>
        </div>
    )
}