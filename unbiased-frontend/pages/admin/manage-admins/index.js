import ModerationPanel from "../../../components/ModerationPanel";
import {useAppContext} from "../../../context";
import Pagination from "../../../components/Pagination";
import Head from "next/head";
import {useEffect, useState} from "react";
import {useRouter} from "next/router";
import {useTriggerEffect} from "../../../utils";
import MainCardsContainer from "../../../components/MainCardsContainer";
import Creator from "../../../components/Creator";
import {userMapper} from "../../../mappers";
import usePagination from "../../../pagination";
import Tooltip from "../../../components/Tooltip";
import {getResourcePath} from "../../../constants";


export default function AddAdmin(props){

    const ctx = useAppContext()
    const I18n = ctx.I18n
    const api = ctx.api
    const router = useRouter()

    const [details, setDetails] = useState(router.query.search|| '')
    const [userList, setUserList] = useState(undefined)
    const [pagination, setPagination] = usePagination()
    const [addAdminMode, setAddAdminMode] = useState(!!router.query.add)

    const [effectTrigger, triggerEffect] = useTriggerEffect()

    useEffect(() => {
        const params = {...router.query, filter: addAdminMode ? 'NOT_ADMINS' : 'ADMINS'}

        api.getUsers(params).then(res => {
            const {success, data, pagination} = res
            setPagination(pagination)
            if (success) {
                setUserList(data)
            }

        })

    }, [router.query, effectTrigger])

    const search = (e) => {
        if (e.key === 'Enter') {
            router.push({
                pathname: router.pathname,
                query: {...(e.target.value && {search: e.target.value})}
            })
        }
    }

    const handleChange = (e) => {
        setDetails(e.target.value)
    }


    const showNotAdmins = async () => {
        const params = {...router.query, filter: addAdminMode ? 'NOT_ADMINS' : 'ADMINS'}

        api.getUsers(params).then(res => {
            const {success, data, pagination} = res
            if (success) {
                setUserList(data)
                setPagination(pagination)
                setAddAdminMode(a => !a)
                router.push({
                        ...router,
                        query: { ...router.query, add: !addAdminMode }
                    },
                    undefined, { shallow: true }
                )
            }

        })
        // triggerEffect()
    }


    return (
        <>
            <Head>
                <title>{I18n("moderation.panel")}</title>
            </Head>
            <div className="d-flex h-100 flex-column">

                <div className="flex-grow-1 d-flex flex-row">

                    <ModerationPanel selected="manage-admins"/>
                    <div className="d-flex flex-column w-75 align-items-center">
                        <div className="w-100 my-3 d-flex flex-row justify-content-center">
                            <div className=" d-flex w-100 m-2 my-lg-0 ">
                                <div className="d-flex w-100 justify-content-center">
                                    <input onKeyDown={search} value={details} id="searchBar_addAdmin" style={{backgroundImage: `url("${getResourcePath("/img/loupe-svgrepo-com.svg")}")`}} className="search-form search form-control text-white w-55"
                                           type="search" placeholder={I18n("moderation.searchAdmin")} onChange={handleChange}/>
                                </div>
                            </div>

                            <Tooltip text={I18n(`tooltip.${addAdminMode ? 'remove' : 'add'}Admin`)}>
                                <button onClick={showNotAdmins}
                                        className="mr-5 mt-1 add-admin-btn bg-transparent border-color-transparent"
                                        style={{backgroundImage: `url(${getResourcePath(`/img/${addAdminMode ? 'less' : 'plus'}-svgrepo-com.svg`)})`}} >
                                </button>
                            </Tooltip>

                            {/*TODO: modal*/}
                        </div>
                        <div className="container-fluid">
                            <MainCardsContainer rows={3} loaded={userList}>
                                {(userList || []).map(c => <Creator toAdd={addAdminMode} admin triggerEffect={triggerEffect} key={`creator${c.id}`} {...c}></Creator>)}
                            </MainCardsContainer>

                        </div>
                    </div>

                </div>
                <Pagination {...pagination}></Pagination>

            </div>
        </>
    )
}