import {useAppContext} from "../../context";
import Link from "next/link";

export default function ServerError() {
    const {I18n} = useAppContext()
    return <div className="d-flex align-items-center justify-content-center h-75">
        <div className="text-center">
            <h1 className="display-1 fw-bold">500</h1>
            <p className="fs-1"> <span className="text-info font-weight-bold">{I18n("genericError.ops")}</span> {I18n("genericError.message.500")}</p>
            <p className="lead">
            </p>
            <Link href="/" className="btn btn-info">{I18n("genericError.button.goHome")}</Link>
    </div>
</div>
}