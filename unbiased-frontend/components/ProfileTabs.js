import {useAppContext} from "../context";
import {useRouter} from "next/router";
import Tabs from "./Tabs";
import PropTypes from "prop-types";
import types from "../types";

export default function ProfileTabs(props) {
    const {I18n, loggedUser} = useAppContext()
    const router = useRouter()
    const orders = [
        { text: I18n("profileCategories.myPosts"), params: {cat: 'MY_POSTS'} },
        { text: I18n("profileCategories.saved"), params: {cat: 'SAVED'}},
        { text: I18n("profileCategories.upvoted"), params: {cat: 'UPVOTED'}},
        { text: I18n("profileCategories.downvoted"), params: {cat: 'DOWNVOTED'}}
    ];

    if (!loggedUser || loggedUser.id !== props.userId) {
        orders.splice(1, 0)
    }

    const orderMap = orders.reduce((a,v) => ({...a, [v.params.order]: v.text}), {})
    const selectedOrder = orderMap[router.query.order] || I18n("profileCategories.myPosts");

    return  <Tabs items={orders} selected={selectedOrder}></Tabs>
}

ProfileTabs.propTypes = types.ProfileTabs