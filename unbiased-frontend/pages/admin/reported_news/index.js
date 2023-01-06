import Moderation_panel from "../../../components/Moderation_panel";
import Tabs from "../../../components/Tabs"
import {useAppContext} from "../../../context";
import PanelCard from "../../../components/PanelCard";

export default function Reported_news(){
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
                    <PanelCard/>
                </div>
            </div>
        </div>
    )
}