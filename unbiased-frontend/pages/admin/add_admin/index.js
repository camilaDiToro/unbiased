import Moderation_panel from "../../../components/Moderation_panel";
import {useAppContext} from "../../../context";
import Tabs from "../../../components/Tabs";

export default function Add_admin(){

    const ctx = useAppContext()
    const items = [{text: ctx.I18n("reportOrder.reportCountDesc"), route: "/admin/reported_news"},
        {text: ctx.I18n("reportOrder.reportCountAsc"), route: "/admin/reported_news"},
        {text: ctx.I18n("reportOrder.reportDateDesc"), route: "/admin/reported_news"},
        {text: ctx.I18n("reportOrder.reportDateAsc"), route: "/admin/reported_news"}]

    return (
        <div className="d-flex h-100 flex-column">
            <div className="flex-grow-1 d-flex flex-row">

                <Moderation_panel/>

                <div className="d-flex flex-column w-75 align-items-center">
                    <Tabs items={items} pill selected={ctx.I18n("reportOrder.reportCountDesc")}/>
                </div>

            </div>
        </div>
    )
}