import ModerationPanel from "../../../components/ModerationPanel";
import {useAppContext} from "../../../context";
import Tabs from "../../../components/Tabs";
import Pagination from "../../../components/Pagination";
import Link from "next/link";
import Head from "next/head";
import i18n from "../../../i18n";
import {useState} from "react";

export default function Add_admin(){

    const ctx = useAppContext()
    const I18n = ctx.I18n
    const items = [{text: ctx.I18n("reportOrder.reportCountDesc"), params: {order: 'REP_COUNT_DESC'}},
        {text: ctx.I18n("reportOrder.reportCountAsc"), params: {order: 'REP_COUNT_ASC'}},
        {text: ctx.I18n("reportOrder.reportDateDesc"), params: {order: 'REP_DATE_DESC'}},
        {text: ctx.I18n("reportOrder.reportDateAsc"), params: {order: 'REP_DATE_ASC'}}
    ]
    const [details, setDetails] = useState({
        searchInput: ""
    })

    const handleChange = (e) => {
        const {name, value} = e.target

        setDetails((prev) => {
            return {...prev, [name]: value}
        })
    }

    return (
        <>
            <Head>
                <title>{I18n("moderation.panel")}</title>
            </Head>
            <div className="d-flex h-100 flex-column">
                <div className="flex-grow-1 d-flex flex-row">

                    <ModerationPanel/>

                    <div className="d-flex flex-column w-75 align-items-center">
                        <div className="w-100 my-3 d-flex flex-row justify-content-center">
                            <div className=" d-flex w-100 form-inline m-2 my-lg-0 ">
                                <div className="d-flex w-100 justify-content-center">
                                    <input id="searchBar_addAdmin" style={{backgroundImage: "url(/img/loupe-svgrepo-com.svg)"}} className="search-form search form-control text-white w-55"
                                           type="search" placeholder={i18n("moderation.searchAdmin")} onChange={handleChange}/>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
                <Pagination currentPage={2} lastPage={4}></Pagination>

            </div>
        </>
    )
}