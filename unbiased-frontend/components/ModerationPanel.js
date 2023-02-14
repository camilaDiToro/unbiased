import {useAppContext} from "../context";
import Link from "next/link";
import {useEffect, useState} from "react";
import {getResourcePath} from "../constants";

export default function ModerationPanel(props){

    const ctx = useAppContext()
    const [splitUrl, setSplitUrl] = useState("none")

    useEffect(()=>{
        setSplitUrl(window.location.href.split('/admin/')[1])
    }, [])

    return(
        <div className="w-25 d-flex flex-column border-white border-right mt-4 m-3">
            <h3 className="text-white">{ctx.I18n("moderation.panel")}</h3>
            <li className="nav-item li-no-dots">
                <div className="d-flex flex-row pt-2 pl-2 bg-hover">

                    {
                        props.selected === 'reported-news' ?
                            <Link className="mb-2 nav-link bold select pl-0" href="/admin/reported-news">
                                <img className="mb-2 moderation-img" src={getResourcePath("/img/warning-svgrepo-com.svg")} alt="..."/>
                                {ctx.I18n("moderation.reportedArticles")}
                            </Link>

                            :

                            <Link className="mb-2 nav-link selected bold pl-0" href="/admin/reported-news">
                                <img className="mb-2 moderation-img" src={getResourcePath("/img/warning-svgrepo-com.svg")} alt="..."/>
                                {ctx.I18n("moderation.reportedArticles")}
                            </Link>
                    }
                </div>
            </li>

            <li className="nav-item li-no-dots">
                <div className="d-flex flex-row pt-2 pl-2 bg-hover">

                    {props.selected === 'reported-comments' ?
                        <Link className="mb-2 nav-link select pl-0" href="/admin/reported-comments">
                            <img className="mb-2 moderation-img" src={getResourcePath("/img/cancel-comment.svg")} alt="..."/>
                            {ctx.I18n("moderation.reportedComments")}
                        </Link>

                        :

                        <Link className="mb-2 nav-link selected pl-0" href="/admin/reported-comments">
                            <img className="mb-2 moderation-img" src={getResourcePath("/img/cancel-comment.svg")} alt="..."/>
                            {ctx.I18n("moderation.reportedComments")}
                        </Link>
                    }
                </div>
            </li>

            <li className="nav-item li-no-dots">
                <div className="d-flex flex-row pt-2 pl-2 bg-hover">

                    {props.selected === 'manage-admins' ?
                        <Link className="mb-2 nav-link select pl-0" href="/admin/manage-admins">
                            <img className="mb-2 moderation-img" src={getResourcePath("/img/add-user-svgrepo-com.svg")} alt="..."/>
                            {ctx.I18n("moderation.manage")}
                        </Link>

                        :

                        <Link className="mb-2 nav-link selected pl-0" href="/admin/manage-admins">
                            <img className="mb-2 moderation-img" src={getResourcePath("/img/add-user-svgrepo-com.svg")} alt="..."/>
                            {ctx.I18n("moderation.manage")}
                        </Link>
                    }

                </div>
            </li>
        </div>
    )
}