import Tabs from "./Tabs";
import {useAppContext} from "../context";
import {useRouter} from "next/router";

export default function ProfileCardTypeTab() {
    const {I18n} = useAppContext()
    const router = useRouter()
    const types = [{text: I18n("home.type.article"), params: {type: 'article'}}, {text: I18n("home.type.creator"), params: {type: 'creator'}}]
    const selectedType = (types.find(t => t.params.type === router.query.type) || types[0]).text

    return               <Tabs items={types} selected={selectedType}></Tabs>

}