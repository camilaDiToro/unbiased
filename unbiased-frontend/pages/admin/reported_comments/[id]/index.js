import {useAppContext} from "../../../../context";
import {useRouter} from "next/router";
import Head from "next/head";
import Modal from "../../../../components/Modal";
import ModerationPanel from "../../../../components/ModerationPanel";
import Link from "next/link";
import ReportReason from "../../../../components/ReportReason";
import {reportInfo} from "../../../../hardcoded";


export async function getServerSideProps(context) {
    return {
        props: {
            reportInfo,
            id: parseInt(context.query.id)
        }, // will be passed to the page component as props
    }
}

export default function ReportedCommentDetail(props){

    const {I18n} = useAppContext()
    const actualReportInfo = props.reportInfo
    const router = useRouter()

    const onDelete = () => {
        alert(`deleted comment of id = ${props.id}`)
        router.push('/admin/reported_comments')
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
                            <img className="svg-btn hover-hand back-btn mt-3 mb-1" src="/img/back-svgrepo-com.svg" alt="..." data-toggle="tooltip" data-placement="bottom" title="Click to go back"/>
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
                            actualReportInfo.map((r)=><ReportReason key={r.user} {...r}></ReportReason>)
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