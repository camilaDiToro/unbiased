import Link from "next/link";
import {useAppContext} from "../../context";
import {useState} from "react";
import {useRouter} from "next/router";
import {getResourcePath} from "../../constants";
import {useSnackbar} from "notistack";

export default function Login() {
    const router = useRouter()
    const ctx = useAppContext()
    const api = ctx.api
    const I18n = ctx.I18n
    const { enqueueSnackbar }= useSnackbar()
    const [details, setDetails] = useState({
        email: "",
        password: ""
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

    async function handleSubmit(e) {
        e.preventDefault()
        const {success} = await api.registerUser(details.email, details.password)
        if (success) {
            enqueueSnackbar(I18n("verificationToken.succesfullySent"))
            await router.push('/login')
        }
    }

    return(
        <div className="d-flex flex-column justify-content-center align-items-center height-100vh">

            <div className="form-signIn d-flex flex-column align-items-center " onSubmit={(e)=>{handleSubmit(e)}}>

                <h1 id="title-log" className="logo mb-3">
                    <Link className="text-info link" href="/">
                        unbiased
                    </Link>
                </h1>

                <h1 className="h3 mb-4 font-weight-normal text-light">{ctx.I18n("navbar.register")}</h1>

                <div className="d-flex mb-4">
                    <img className="size-img-modal-login align-self-center" src={getResourcePath("/img/profile-svgrepo-com.svg")} alt="..."/>
                    <input onChange={handleChange} type="text" id="email" name="email" placeholder="EmailAddress" className="sign-form-control" required="" autoFocus=""/>
                </div>

                <div className="mb-2 mt-1 d-flex flex-row justify-content-center align-items-center position-relative">
                    <img src={getResourcePath("/img/lock-svgrepo-com.svg")} alt="..." className="size-img-modal-login align-self-center"/>
                    <input type={passwordType} onChange={handleChange} name="password" placeholder="Password" className="sign-form-control h-fit mb-1"/>
                    <button className="btn  eye-button-properties" onClick={togglePassword}>
                        { passwordType==="password"? <img src={getResourcePath("/img/eye.svg")}/> : <img src={getResourcePath("/img/eye-slash.svg")}/> }
                    </button>

                </div>


                <button onClick={handleSubmit} type="submit" className="btn btn-md btn-info btn-block">{ctx.I18n("navbar.register")}</button>
                <div className="d-flex mt-2">
                    <div className="mr-2">
                        {I18n("register.alreadyMemberQuestion")}
                    </div>
                    <Link href={`/login`} className="link text-underline blue-text">{I18n("navbar.logIn")}</Link>{" "}
                </div>
                <p className="mt-5 mb-3 text-muted">?? 2022-2022</p>
            </div>
        </div>
    )
}