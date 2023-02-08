import Moderation_panel from "../../../components/ModerationPanel";
import Tabs from "../../../components/Tabs"
import {useAppContext} from "../../../context";
import ReportedCard from "../../../components/ReportedCard";
import Link from "next/link";
import {news} from "../../../hardcoded"
import {useEffect, useState} from "react";
import {useTriggerEffect} from "../../../utils";
import {useRouter} from "next/router";
import AdminOrderTabs from "../../../components/AdminOrderTabs";
import Pagination from "../../../components/Pagination";
import Head from "next/head";
import {newsMapper} from "../../../mappers";
import usePagination from "../../../pagination";


export default function Reported_news(){
    const {I18n, axios}= useAppContext()
    const [pagination, setPagination] = usePagination()
    const router = useRouter()
    const [reportedNews, setReportedNews] = useState([])
    const [effectTrigger, triggerEffect] = useTriggerEffect()

    useEffect(() => {
        const params = {page: router.query.page, reportOrder: router.query.order, reported: true}
        axios.get('news', {params}).then(res => {
            setPagination(res)
            const mappedNews = (res.data || []).map(newsMapper)
            setReportedNews(mappedNews)
        })
    }, [router.query, effectTrigger])

    return (
        <>
            <Head>
                <title>{I18n("moderation.panel")}</title>
            </Head>
            <div className="d-flex h-100 flex-column">
                <div className="flex-grow-1 d-flex flex-row bg-fixed">
                    <Moderation_panel/>
                    <div className="d-flex flex-column w-75 align-items-center">
                        <AdminOrderTabs></AdminOrderTabs>
                        {
                            reportedNews.map((n) => {
                                return (
                                    <ReportedCard triggerEffect={triggerEffect} key={n.id} {...n}/>
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