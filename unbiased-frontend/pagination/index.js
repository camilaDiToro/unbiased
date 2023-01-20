import {useState} from "react";
import parse from "parse-link-header";
import {useRouter} from "next/router";




export default function usePagination() {
    const [pagination, setPagination] = useState({currentPage: 1, lastPage: 1})
    const router = useRouter()
    const set = async (res) => {
        if (res.status !== 200) {
            setPagination(null)
            return
        }
        const maybeCurrent = parseInt(router.query.page || '1')
        const parsedLink = parse(res.headers.get('Link'))
        const last = parseInt(parsedLink.last.page)
        console.log('A')
        console.log(JSON.stringify(parsedLink))
        if (maybeCurrent > last) {
            await router.push({
                pathname: router.pathname,
                query: {...router.query, page: last}
            })
        } else if (maybeCurrent < 1) {
            await router.push({
                pathname: router.pathname,
                query: {...router.query, page: 1}
            })
        } else {
            setPagination({currentPage: maybeCurrent, lastPage: last})
            // res.json().then(j => alert(JSON.stringify(j)))
        }
    }

    return [pagination, set]
}