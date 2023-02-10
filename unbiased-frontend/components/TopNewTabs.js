import Tabs from "./Tabs";
import {useRouter} from "next/router";
import {useAppContext} from "../context";
import types from "../types";

export default function TopNewTabs(props) {
    const {I18n} = useAppContext()
    const router = useRouter()
    const orders = [
        { text: I18n("order.new"), params: {order: 'NEW'} },
        { text: I18n("order.top"), params: {order: 'TOP'} }
    ];



    const orderMap = orders.reduce((a,v) => ({...a, [v.params.order]: v.text}), {})
    const selectedOrder = orderMap[router.query.order] || I18n("order.top");

    return  <Tabs className={props.className} items={orders} pill selected={selectedOrder}>
        {props.children}
    </Tabs>

}

TopNewTabs.propTypes = types.TopNewTabs