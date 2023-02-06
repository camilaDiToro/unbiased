import Link from "next/link";
import {useAppContext} from "../context";

export default function NewsCategoryPills(props) {
    const {I18n} = useAppContext()
    const categoryMap = {
        "categories.tourism": "TOURISM",
        "categories.entertainment": "SHOW",
        "categories.politics": "POLITICS",
        "categories.economics": "ECONOMICS",
        "categories.sports": "SPORTS",
        "categories.technology": "TECHNOLOGY"
    }
   return <>
        { props.categories.map(c => <Link key={c} href={{
            pathname: "/",
            query: { cat: categoryMap[c] },
        }}>
            <span id="span_category" className="badge badge-pill badge-info">
                {I18n(c)}
            </span>
        </Link>)}
    </>

}