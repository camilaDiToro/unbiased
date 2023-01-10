import Moderation_panel from "../../../components/ModerationPanel";
import Tabs from "../../../components/Tabs"
import {useAppContext} from "../../../context";
import ReportedCard from "../../../components/ReportedCard";
import Link from "next/link";
import {news} from "../../../hardcoded"
import {useEffect, useState} from "react";

export async function getServerSideProps(context) {
    return {
        props: {
            news
        }, // will be passed to the page component as props
    }
}

export default function Reported_news(){
    const ctx = useAppContext()
    const items = [{text: ctx.I18n("reportOrder.reportCountDesc"), route: "/admin/reported_news"},
                    {text: ctx.I18n("reportOrder.reportCountAsc"), route: "/admin/reported_news"},
                    {text: ctx.I18n("reportOrder.reportDateDesc"), route: "/admin/reported_news"},
                    {text: ctx.I18n("reportOrder.reportDateAsc"), route: "/admin/reported_news"}]


    return (
        <div className="d-flex h-100 flex-column">
            <div className="flex-grow-1 d-flex flex-row bg-fixed">
                <Moderation_panel/>
                <div className="d-flex flex-column w-75 align-items-center">
                    <Tabs items={items} pill selected={ctx.I18n("reportOrder.reportCountDesc")}/>

                    {
                        news.map((n) => {
                            return (
                                <ReportedCard key={n.id} {...n}/>
                            )
                        })
                    }

                </div>
            </div>
            {/*TODO: if not empty newList and pagination*/}
        </div>
    )
}