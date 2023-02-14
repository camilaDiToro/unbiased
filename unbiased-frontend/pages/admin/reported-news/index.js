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
import MainCardsContainer from "../../../components/MainCardsContainer";


export default function Reported_news(){
    const {I18n, api}= useAppContext()
    const [pagination, setPagination] = usePagination()
    const router = useRouter()
    const [reportedNews, setReportedNews] = useState(undefined)
    const [effectTrigger, triggerEffect] = useTriggerEffect()

    useEffect(() => {
        const params = {page: router.query.page, order: router.query.order, filter: 'REPORTED'}
        api.getArticles(params).then(res => {
            const {data, pagination, success} = res
            if (success) {
                setPagination(pagination)
                setReportedNews(data)
            }
        })
    }, [router.query, effectTrigger])

    return (
        <>
            <Head>
                <title>{I18n("moderation.panel")}</title>
            </Head>
            <div className="d-flex h-100 flex-column">
                <div className="flex-grow-1 d-flex flex-row bg-fixed">
                    <Moderation_panel selected="reported-news"/>
                    <div className="d-flex flex-column w-75 align-items-center">
                        <AdminOrderTabs></AdminOrderTabs>
                        <MainCardsContainer reported loaded={reportedNews}>
                            {
                                (reportedNews||[]).map((n) => {
                                    return (
                                        <ReportedCard triggerEffect={triggerEffect} key={n.id} {...n}/>
                                    )
                                })
                            }
                        </MainCardsContainer>

                    </div>
                </div>
                <Pagination {...pagination}></Pagination>
            </div>
        </>
    )
}