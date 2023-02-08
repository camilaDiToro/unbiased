import {useAppContext} from "../../../../context";
import {useRouter} from "next/router";
import Head from "next/head";
import Modal from "../../../../components/Modal";
import ModerationPanel from "../../../../components/ModerationPanel";
import Link from "next/link";
import ReportReason from "../../../../components/ReportReason";
import {useEffect, useState} from "react";
import {getResourcePath} from "../../../../constants";



export default function ReportedCommentDetail(props){

    const {I18n, axios} = useAppContext()
    const router = useRouter()

    const [reportInfo, setReportInfo] = useState([])
    const {id} = router.query
    useEffect(() => {
        if (!id)
            return
        axios.get(`comments/${id}/reports`).then (res => {
            setReportInfo(res.data || [])
        })
    }, [id])


    const onDelete = async () => {
        await axios.delete(`comments/${id}`)
        await router.push('/admin/reported_comments')
    }
    return (<>
            <Head>
                <title>{I18n("moderation.panel")}</title>
            </Head>
            <div className="d-flex flex-row h-100">
                <Modal onClickHandler={onDelete} id="binModal" title={I18n("showNews.deleteCommentQuestion")} body={I18n("showNews.deleteCommentBody")} acceptText={I18n("moderation.delete")}/>
                <ModerationPanel/>

                <div className="d-flex w-75 flex-column">
                    <div className="w-100 my-2">
                        <Link href="/admin/reported_news">
                            <img className="svg-btn hover-hand back-btn mt-3 mb-1" src={getResourcePath("/img/back-svgrepo-com.svg")} alt="..." data-toggle="tooltip" data-placement="bottom" title="Click to go back"/>
                        </Link>
                    </div>

                    <table className="table flex-grow-0 text-white">
                        <thead>
                        <tr>
                            <th scope="col">{I18n("moderation.user")}</th>
                            <th scope="col">{I18n("moderation.reason")}</th>
                            <th scope="col">{I18n("moderation.date")}</th>
                        </tr>
                        </thead>
                        <tbody>
                        {
                            reportInfo.map((r)=><ReportReason key={r.user} {...r}></ReportReason>)
                        }
                        </tbody>
                    </table>

                    <button data-toggle="modal" data-target="#binModal" className="btn btn-danger delete-btn">{I18n("moderation.deleteComment")}</button>
                </div>

                {/*TODO: Hay un if al final, agregar*/}
            </div>

        </>
    )
}