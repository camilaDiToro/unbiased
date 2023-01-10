import TimeAgo from "javascript-time-ago";
import es from "javascript-time-ago/locale/es";
import en from 'javascript-time-ago/locale/en'
import {useEffect, useState} from "react";
import {useRouter} from "next/router";

export default function FormattedDate(props) {
    const { locale } = useRouter();
    TimeAgo.addLocale(es)
    TimeAgo.addDefaultLocale(en)

    const [useTimeAgo, setTimeAgo] = useState('')
    // const [useSaved, setSaved] = useState(props.saved)
    // const [usePinned, setPinned] = useState(props.pinned)
    const timeAgo = new TimeAgo(locale)
    const date = new Date(props.datetime)

    useEffect(() => setTimeAgo(timeAgo.format(date)), [])

    if (props.timeAgo) {
        return useTimeAgo
    } else {
        const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };

        return date.toLocaleDateString(locale, options)
    }
}