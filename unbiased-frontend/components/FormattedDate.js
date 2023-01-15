import TimeAgo from "javascript-time-ago";

import {useEffect, useState} from "react";
import {useRouter} from "next/router";

export default function FormattedDate(props) {
    const { locale } = useRouter();
    const timeAgo = new TimeAgo(locale)
    const date = new Date(props.datetime)

    const [useTimeAgo, setTimeAgo] = useState(timeAgo.format(date))
    // const [useSaved, setSaved] = useState(props.saved)
    // const [usePinned, setPinned] = useState(props.pinned)


    // useEffect(() => setTimeAgo(), [])

    if (props.timeAgo) {
        return useTimeAgo
    } else {
        const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };

        return date.toLocaleDateString(locale, options)
    }
}