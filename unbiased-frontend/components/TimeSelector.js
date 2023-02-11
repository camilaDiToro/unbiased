import {useState} from "react";
import {useAppContext} from "../context";
import {useRouter} from "next/router";
import Link from "next/link";

export default function TimeSelector() {
    const {I18n} = useAppContext()
    const router = useRouter()
    const categories = [
        { text: I18n("timeConstraint.hour"), params: {time: 'HOUR'} },
        { text: I18n("timeConstraint.day"), params: {time: 'DAY'} },
        { text: I18n("timeConstraint.week"), params: {time: 'WEEK'} },
        { text: I18n("timeConstraint.alltime"), params: {time: 'ALLTIME'} },
    ];


    const timeMap = categories.reduce((a,v) => ({...a, [v.params.time]: v.text}), {})


    const [timeConstraint, setTimeConstraint] = useState(router.query.time || 'WEEK')
    return <>
        <div className="btn-group ">
            <div className="btn dropdown-toggle text-white" data-toggle="dropdown">
                {timeMap[timeConstraint]}
            </div>

            <ul className="dropdown-menu bg-dropdown">
                {categories.map(c => (<li key={c.text} className="mb-1"><Link onClick={(e) => setTimeConstraint(c.params.time)} className="dropdown-items" href={{
                        pathname: router.pathname,
                        query: { ...router.query, time: c.params.time },
                    }}>
                        {c.text}
                    </Link></li>))
                }
                    </ul>
        </div>
    </>
}