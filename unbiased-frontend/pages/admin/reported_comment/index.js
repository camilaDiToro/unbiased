import Moderation_panel from "../../../components/ModerationPanel";
import {useAppContext} from "../../../context";
import Tabs from "../../../components/Tabs";
import {useEffect, useState} from "react";
import {useTriggerEffect} from "../../../utils";
import {useRouter} from "next/router";
import AdminOrderTabs from "../../../components/AdminOrderTabs";
import ReportedCard from "../../../components/ReportedCard";
import {news} from "../../../hardcoded";

export async function getServerSideProps(context) {
    return {
        props: {
            news
        }, // will be passed to the page component as props
    }
}

export default function Reported_comments(props){

    const ctx = useAppContext()
    const items = [{text: ctx.I18n("reportOrder.reportCountDesc"), route: "/admin/reported_comments"},
        {text: ctx.I18n("reportOrder.reportCountAsc"), route: "/admin/reported_comments"},
        {text: ctx.I18n("reportOrder.reportDateDesc"), route: "/admin/reported_comments"},
        {text: ctx.I18n("reportOrder.reportDateAsc"), route: "/admin/reported_comments"}]
    const router = useRouter()
    const [reportedComments, setReportedComments] = useState(props.news[0].comments)
    const [effectTrigger, triggerEffect] = useTriggerEffect()

    useEffect(() => {
        setReportedComments(n => {
            for (const comment of n) {
                comment.body+= 'a'
            }
            return n
        })
    }, [router.query, effectTrigger])
    return (
        <div className="d-flex h-100 flex-column">
            <div className="flex-grow-1 d-flex flex-row">

                <Moderation_panel/>

                <div className="d-flex flex-column w-75 align-items-center">
                    <AdminOrderTabs></AdminOrderTabs>
                    {
                        reportedComments.map((n) => {
                            return (
                                <ReportedCard comment triggerEffect={triggerEffect} key={n.id} {...n}/>
                            )
                        })
                    }
                </div>

            </div>
        </div>
    )
}