import ModerationPanel from "../../../../components/ModerationPanel";
import Link from "next/link";
import {reportInfo} from "hardcoded"
import {useAppContext} from "../../../../context";
import {useRouter} from "next/router";
import {useState} from "react";
import Modal from "../../../../components/Modal";

export async function getServerSideProps(context) {
    return {
        props: {
            reportInfo
        }, // will be passed to the page component as props
    }
}

export default function ReportedNewsDetail(props){
    const ctx = useAppContext()
    const [actualReportInfo, setActualReportInfo] = useState(props.reportInfo)

    const onDelete = () => {
        //TODO: borrar articulo
    }

    return (
        <div className="d-flex flex-row">
            <Modal onClickHandler={onDelete} id="binModal" title={ctx.I18n("moderation.delete")} body={ctx.I18n("showNews.deleteCommentBody")}/>
            <ModerationPanel/>

            <div className="d-flex w-75 flex-column">
                <div className="w-100 my-2">
                    <Link href="/admin/reported_news">
                        <img className="svg-btn hover-hand back-btn mt-3 mb-1" src="/img/back-svgrepo-com.svg" alt="..." data-toggle="tooltip" data-placement="bottom" title="Click to go back"/>
                    </Link>
                </div>

                <table className="table flex-grow-0 text-white">
                    <thead>
                    <tr>
                        <th scope="col">{ctx.I18n("moderation.user")}</th>
                        <th scope="col">{ctx.I18n("moderation.reason")}</th>
                        <th scope="col">{ctx.I18n("moderation.date")}</th>
                    </tr>
                    </thead>
                    <tbody>
                        {
                            actualReportInfo.map((r)=>{
                                return (
                                    <tr key={r.id}>
                                        <td>{r.user}</td>
                                        <td>{r.reason}</td>
                                        <td>{r.datetime}</td>
                                    </tr>
                                )
                            })
                        }
                    </tbody>
                </table>

                <button data-toggle="modal" data-target="#binModal" className="btn btn-danger delete-btn">Delete article</button>
            </div>

            {/*TODO: Hay un if al final, agregar*/}
        </div>
    )
}