import TimeAgo from "javascript-time-ago";

import {useState} from "react";
import {useRouter} from "next/router";
import types from "../types";

export default function FormattedDate(props) {
    const { locale } = useRouter();
    const timeAgo = new TimeAgo(locale)
    const date = new Date(props.datetime)

    const [useTimeAgo, setTimeAgo] = useState(timeAgo.format(date))


    if (props.timeAgo) {
        return useTimeAgo
    } else {
        const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };

        return date.toLocaleDateString(locale, options)
    }
}

FormattedDate.propTypes = types.FormattedDate