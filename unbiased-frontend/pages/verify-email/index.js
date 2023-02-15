import {useAppContext} from "../../context";
import Link from "next/link";
import Tooltip from "../../components/Tooltip";
import {useEffect, useState} from "react";
import {useRouter} from "next/router";

export default function EmailVerified(){
    const {api} = useAppContext()
    const router = useRouter()
    const {token} = router.query
    const [verified, setVerified] = useState(false)
    useEffect(() => {
        if (!token)
            return
        api.verifyEmail(token).then(r => {
            const {success} = r
            if (success) {
                setVerified(true)
            } else {
                // router.replace("/")
            }
        })
    }, [token])
    const {I18n} = useAppContext()

    if (!verified) {
        return <></>
    }
    return (
        <div className="w-100 h-100 d-flex justify-content-center align-items-center">
            <div>


                <h1 id="title-log" className="logo mb-5 d-flex justify-content-center">
                    <Tooltip position="bottom" text={I18n("tooltip.clickToGoHome")}>
                        <Link className="text-info link" href="/">
                            unbiased
                        </Link>
                    </Tooltip>
                </h1>

                <h1 className="h3 mb-3 font-weight-normal text-light">{I18n("verificationToken.succesfullyVerified")}</h1>
            </div>
        </div>
    )
}