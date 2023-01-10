import Link from "next/link";
import {useAppContext} from "../../context";
import {useState} from "react";

export default function Login() {

    const ctx = useAppContext()
    const [details, setDetails] = useState({
        username: "",
        password: "",
        rememberMe: false
    })
    const [passwordType, setPasswordType] = useState("password");

    const handleChange = (e) => {

        const {name, value} = e.target

        console.log(name, value)

        if (name !== "rememberMe") {
            setDetails((prev) => {
                return {...prev, [name]: value}
            })
        }

        setDetails((prev) => {
            return {...prev, [name]: !details.rememberMe}
        })
    }

    const togglePassword = () => {
        if (passwordType === "password") {
            setPasswordType("text")
            return;
        }
        setPasswordType("password")
    }

    function handleSubmit(e) {
        e.preventDefault()
        //TODO: post del objeto
    }

    return(
        <div className="text-center d-flex flex-column align-content-center">
            <h1 id="title-log" className="logo mb-4">
                <Link className="text-info link" href="/">
                    unbiased
                </Link>
            </h1>

            {/*TODO: i18n to all inputs*/}
            <div className="form-signIn d-flex flex-column align-items-center" onSubmit={(e)=>{handleSubmit(e)}}>
                <div className="d-flex mb-4">
                    <img className="size-img-modal-login align-self-center" src="/img/profile-svgrepo-com.svg" alt="..."/>
                    <input onChange={handleChange} type="text" id="username" name="username" placeholder="EmailAddress" className="sign-form-control" required="" autoFocus=""/>
                </div>

                <div className="mt-1 d-flex justify-content-center align-items-center position-relative">
                    <img src="/img/lock-svgrepo-com.svg" alt="..." className="size-img-modal-login align-self-center"/>
                    <input type={passwordType} onChange={handleChange} name="password" placeholder="Password" className="sign-form-control h-fit mb-1"/>
                    <div className="input-group-btn">
                        <button className="btn btn-outline-primary" onClick={togglePassword}>
                            { passwordType==="password"? <i className="bi bi-eye-slash"></i> : <i className="bi bi-eye"></i> }
                        </button>
                    </div>
                </div>

                <div className="checkbox mb-3">
                    <label className="text-light">
                        <input onChange={handleChange} type="checkbox" name="rememberMe"/>
                        Remember me
                    </label>
                </div>

                <button type="submit" className="btn btn-md btn-info btn-block">Sign in</button>
                <p className="mt-5 mb-3 text-muted">© 2022-2022</p>
            </div>
        </div>
    )
}