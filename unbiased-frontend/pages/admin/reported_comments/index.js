import Moderation_panel from "../../../components/ModerationPanel";
import {useAppContext} from "../../../context";
import Tabs from "../../../components/Tabs";
import {useEffect, useState} from "react";
import {useTriggerEffect} from "../../../utils";
import {useRouter} from "next/router";
import AdminOrderTabs from "../../../components/AdminOrderTabs";
import ReportedCard from "../../../components/ReportedCard";
import {news} from "../../../hardcoded";
import Pagination from "../../../components/Pagination";
import Link from "next/link";
import ModerationPanel from "../../../components/ModerationPanel";
import Head from "next/head";
import {commentsMapper} from "../../../mappers";
import usePagination from "../../../pagination";


export default function Reported_comments(){

    const ctx = useAppContext()
    const I18n = ctx.I18n

    const router = useRouter()
    const [reportedComments, setReportedComments] = useState([])
    const [effectTrigger, triggerEffect] = useTriggerEffect()
    const [pagination, setPagination] = usePagination()
    useEffect(() => {
        const params = {reportOrder: router.query.order, reported: true, page: router.query.page}
        ctx.api.getComments(params).then(res => {
            const {pagination, success, data} = res
            if (success) {
                setPagination(pagination)
                setReportedComments(data)
            }
        })
    }, [router.query, effectTrigger])
    return (
        <>
            <Head>
                <title>{I18n("moderation.panel")}</title>
            </Head>
            <div className="d-flex h-100 flex-column">
                <div className="flex-grow-1 d-flex flex-row">

                    <Moderation_panel/>

                    <div className="d-flex flex-column w-75 align-items-center">
                        <AdminOrderTabs></AdminOrderTabs>
                        {
                            reportedComments.map((n) => {
                                return (
                                    <ReportedCard comment triggerEffect={triggerEffect} key={n.id} {...n} title={n.body}/>
                                )
                            })
                        }
                    </div>
                </div>
                <Pagination {...pagination}></Pagination>

            </div>
        </>
    )
}