import {useAppContext} from "../context";
import Link from "next/link";

//TODO: Hay que agregar a todos el color azul al que este seleccionado
export default function ModerationPanel(){

    const ctx = useAppContext()

    return(
        <div className="w-25 d-flex flex-column border-white border-right mt-4 m-3">
            <h3 className="text-white">{ctx.I18n("moderation.panel")}</h3>
            <li className="nav-item li-no-dots">
                <div className="d-flex flex-row pt-2 pl-2 bg-hover">
                    <Link className="mb-2 nav-link selected pl-0" href="/">
                        <img className="mb-2 moderation-img" src="/img/warning-svgrepo-com.svg" alt="..."/>
                        {ctx.I18n("moderation.reportedArticles")}
                    </Link>
                </div>
            </li>

            <li className="nav-item li-no-dots">
                <div className="d-flex flex-row pt-2 pl-2 bg-hover">
                    <Link className="mb-2 nav-link selected pl-0" href="/">
                        <img className="mb-2 moderation-img" src="/img/cancel-comment.svg" alt="..."/>
                        {ctx.I18n("moderation.reportedComments")}
                    </Link>
                </div>
            </li>

            {/*TODO: agregar if is owner*/}
            <li className="nav-item li-no-dots">
                <div className="d-flex flex-row pt-2 pl-2 bg-hover">
                    <Link className="mb-2 nav-link selected pl-0" href="/">
                        <img className="mb-2 moderation-img" src="/img/add-user-svgrepo-com.svg" alt="..."/>
                        {ctx.I18n("moderation.reportedComments")}
                    </Link>
                </div>
            </li>
        </div>
    )
}