import Moderation_panel from "../../../components/ModerationPanel";
import Tabs from "../../../components/Tabs"
import {useAppContext} from "../../../context";
import PanelCard from "../../../components/PanelCard";
import Link from "next/link";

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
                        [0,1,2].map((i) => {
                            return (
                                <PanelCard key={i} title={"TITULO"} subtitle={"Subtitulo"} timeAmount={null}
                                           name={"Nombre"} profileImg={null} reportsCount={null}
                                />
                            )
                        })
                    }

                </div>
            </div>
            {/*TODO: if not empty newList and pagination*/}
        </div>
    )
}