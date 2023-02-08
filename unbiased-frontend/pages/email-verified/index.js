import {useAppContext} from "../../context";
import I18n from "../../i18n";
import Link from "next/link";
import Tooltip from "../../components/Tooltip";
import i18n from "../../i18n";

export default function EmailVerified(){

    const {ctx} = useAppContext()
    return (
        <div className="w-100 h-100 d-flex justify-content-center align-items-center">
            <div>


                <h1 id="title-log" className="logo mb-5 d-flex justify-content-center">
                    <Tooltip position="bottom" text={i18n("tooltip.clickToGoHome")}>
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