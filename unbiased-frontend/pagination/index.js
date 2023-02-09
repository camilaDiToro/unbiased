import {useState} from "react";
import parse from "parse-link-header";
import {useRouter} from "next/router";




export default function usePagination() {
    const [pagination, setPagination] = useState({currentPage: 1, lastPage: 1})
    const router = useRouter()
    const set = async (pagination) => {
        const {currentPage, lastPage} = pagination
        if (currentPage > lastPage) {
            await router.push({
                pathname: router.pathname,
                query: {...router.query, page: lastPage}
            })
            return
        } else if (currentPage < 1) {
            await router.push({
                pathname: router.pathname,
                query: {...router.query, page: 1}
            })
            return
        }
        setPagination(pagination)

    }

    return [pagination, set]
}