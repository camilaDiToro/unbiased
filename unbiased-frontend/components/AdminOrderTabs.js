import {useRouter} from "next/router";
import Tabs from "./Tabs";
import {useAppContext} from "../context";

export default function AdminOrderTabs(props) {
    const router = useRouter()
    const {I18n} = useAppContext()
    const orders= [{text: I18n("reportOrder.reportCountDesc"), params: {order: "REP_COUNT_DESC"}},
        {text: I18n("reportOrder.reportCountAsc"), params: {order: "REP_COUNT_DESC"}},
        {text: I18n("reportOrder.reportDateDesc"), params: {order: "REP_DATE_DESC"}},
        {text: I18n("reportOrder.reportDateAsc"), params: {order: "REP_DATE_ASC"}}]

    const orderMap = orders.reduce((a,v) => ({...a, [v.params.order]: v.text}), {})
    const selectedOrder = orderMap[router.query.order] || I18n("reportOrder.reportCountDesc");

    return  <Tabs pill items={orders} selected={selectedOrder}></Tabs>
}