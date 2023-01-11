import Link from "next/link";
import {useAppContext} from "../context";

export default function NewsCategoryPills(props) {
    const {I18n} = useAppContext()
    const categoryMap = {
        TOURISM: I18n("categories.tourism"),
        SHOW: I18n("categories.entertainment"),
        POLITICS:I18n("categories.politics"),
        ECONOMICS: I18n("categories.economics"),
        SPORTS:  I18n("categories.sports"),
        TECHNOLOGY: I18n("categories.technology")
    }
   return <>
        { props.categories.map(c => <Link key={c} href={{
            pathname: "/",
            query: { cat: c },
        }}>
            <span id="span_category" className="badge badge-pill badge-info">
                {categoryMap[c]}
            </span>
        </Link>)}
    </>

}