import ModerationPanel from "../../../components/ModerationPanel";
import {useAppContext} from "../../../context";
import Tabs from "../../../components/Tabs";
import Pagination from "../../../components/Pagination";
import Link from "next/link";
import Head from "next/head";

export default function Add_admin(){

    const ctx = useAppContext()
    const I18n = ctx.I18n
    const items = [{text: ctx.I18n("reportOrder.reportCountDesc"), params: {order: 'REP_COUNT_DESC'}},
        {text: ctx.I18n("reportOrder.reportCountAsc"), params: {order: 'REP_COUNT_ASC'}},
        {text: ctx.I18n("reportOrder.reportDateDesc"), params: {order: 'REP_DATE_DESC'}},
        {text: ctx.I18n("reportOrder.reportDateAsc"), params: {order: 'REP_DATE_ASC'}}
]

    return (
        <>
            <Head>
                <title>{I18n("moderation.panel")}</title>
            </Head>
            <div className="d-flex h-100 flex-column">
                <div className="flex-grow-1 d-flex flex-row">

                    <ModerationPanel/>

                    <div className="d-flex flex-column w-75 align-items-center">
                        <Tabs items={items} pill selected={ctx.I18n("reportOrder.reportCountDesc")}/>
                    </div>

                </div>
                <Pagination currentPage={2} lastPage={4}></Pagination>

            </div></>
    )
}