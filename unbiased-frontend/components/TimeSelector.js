import {useEffect, useState} from "react";
import {useAppContext} from "../context";
import {useRouter} from "next/router";
import Link from "next/link";

export default function TimeSelector() {
    const {I18n} = useAppContext()
    const router = useRouter()
    const {time} = router.query
    useEffect(() => {

    }, [time])
    const categories = [
        { text: I18n("timeConstraint.hour"), params: {time: 'HOUR'} },
        { text: I18n("timeConstraint.day"), params: {time: 'DAY'} },
        { text: I18n("timeConstraint.week"), params: {time: 'WEEK'} },
        { text: I18n("timeConstraint.alltime"), params: {time: 'ALLTIME'} },
    ];


    const timeMap = categories.reduce((a,v) => ({...a, [v.params.time]: v.text}), {})


    return <>
        <div className="btn-group ">
            <div data-testid="selected-time" className="btn dropdown-toggle text-white" data-toggle="dropdown">
                {timeMap[time || 'WEEK']}
            </div>

            <ul className="dropdown-menu bg-dropdown">
                {categories.map(c => (<li key={c.text} className="mb-1"><Link className="dropdown-items" href={{
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