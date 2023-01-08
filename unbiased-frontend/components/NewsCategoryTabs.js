import {useAppContext} from "../context";
import Tabs from "./Tabs";

export default function NewsCategoryTabs() {
    const {I18n} = useAppContext()
    const categories = [
        { text: I18n("categories.all"), params: {cat: 'ALL'} },
        { text: I18n("categories.tourism"), params: {cat: 'TOURISM'} },
        { text: I18n("categories.entertainment"), params: {cat: 'SHOW'} },
        { text: I18n("categories.politics"), params: {cat: 'POLITICS'} },
        { text: I18n("categories.economics"), params: {cat: 'ECONOMICS'} },
        { text: I18n("categories.sports"), params: {cat: 'SPORTS'} },
        { text: I18n("categories.technology"), params: {cat: 'TECHNOLOGY'} }
    ];

    const categoryMap = categories.reduce((a,v) => ({...a, [v.params.cat]: v.text}), {})

    const selectedCategory = categoryMap[router.query.cat] || I18n("categories.all")

    return <Tabs items={categories} selected={selectedCategory}></Tabs>
}