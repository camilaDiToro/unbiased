import ModerationPanel from "../../../../components/ModerationPanel";
import Link from "next/link";
import {reportInfo} from "hardcoded"
import {useAppContext} from "../../../../context";
import {useRouter} from "next/router";
import {useEffect, useState} from "react";
import Modal from "../../../../components/Modal";
import ReportReason from "../../../../components/ReportReason";
import Head from "next/head";
import {getResourcePath} from "../../../../constants";
import Tooltip from "../../../../components/Tooltip"


export default function ReportedNewsDetail(props){
    const {I18n, api} = useAppContext()
    const [reportInfo, setReportInfo] = useState([])
    const router = useRouter()
    const {id} = router.query

    useEffect(() => {
        if (!id)
            return
        api.getArticleReports(id).then(r => {
            const {success, data} = r
            success && setReportInfo(data)
        })
    }, [id])

    const onDelete = async () => {
        const {success} = await api.deleteArticle(id)
        if (success) {
            await router.push('/admin/reported-news')
        }
    }
    return (<>
            <Head>
                <title>{I18n("moderation.panel")}</title>
            </Head>
            <div className="d-flex flex-row h-100">
                <Modal onClickHandler={onDelete} id="binModal" title={I18n("profile.modal.question")} body={I18n("profile.modal.msg")} acceptText={I18n("moderation.delete")}/>
                <ModerationPanel/>

                <div className="d-flex w-75 flex-column">
                    <div className="w-100 my-2">
                        <Link href="/admin/reported-news">
                            <Tooltip position="bottom" text={I18n("tooltip.clickToGoBack")}>
                                <img className="svg-btn hover-hand back-btn mt-3 mb-1" src={getResourcePath("/img/back-svgrepo-com.svg")} alt="..." />
                            </Tooltip>
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

                    <button data-toggle="modal" data-target="#binModal" className="btn btn-danger delete-btn">{I18n("moderation.delete")}</button>
                </div>

                {/*TODO: Hay un if al final, agregar*/}
            </div>

        </>
    )
}