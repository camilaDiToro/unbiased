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

export async function getServerSideProps(context) {
    return {
        props: {
            news
        }, // will be passed to the page component as props
    }
}

export default function Reported_news(props){
    const ctx = useAppContext()
    const router = useRouter()
    const [reportedNews, setReportedNews] = useState(props.news)
    const [effectTrigger, triggerEffect] = useTriggerEffect()

    useEffect(() => {
        setReportedNews(n => {
            for (const news of n) {
                news.title += 'a'
            }
            return n
        })
    }, [router.query, effectTrigger])

    return (
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
            {/*TODO: if not empty newList and pagination*/}
        </div>
    )
}