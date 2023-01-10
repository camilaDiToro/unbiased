import Moderation_panel from "../../../components/ModerationPanel";
import {useAppContext} from "../../../context";
import Tabs from "../../../components/Tabs";

export default function Reported_comments(){

    const ctx = useAppContext()
    const items = [{text: ctx.I18n("reportOrder.reportCountDesc"), route: "/admin/reported_comments"},
        {text: ctx.I18n("reportOrder.reportCountAsc"), route: "/admin/reported_comments"},
        {text: ctx.I18n("reportOrder.reportDateDesc"), route: "/admin/reported_comments"},
        {text: ctx.I18n("reportOrder.reportDateAsc"), route: "/admin/reported_comments"}]

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