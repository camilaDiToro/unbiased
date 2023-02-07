import ModerationPanel from "../../../components/ModerationPanel";
import {useAppContext} from "../../../context";
import Pagination from "../../../components/Pagination";
import Head from "next/head";
import i18n from "../../../i18n";
import {useEffect, useState} from "react";
import {useRouter} from "next/router";
import {useTriggerEffect} from "../../../utils";
import {users} from "../../../hardcoded";
import MainCardsContainer from "../../../components/MainCardsContainer";
import Creator from "../../../components/Creator";
import Modal from "../../../components/Modal";
import {userMapper} from "../../../mappers";
import usePagination from "../../../pagination";

export async function getServerSideProps(context) {
    return {
        props: {
            users: users.filter(user => user.nameOrEmail.includes(context.query.query || ''))
        }, // will be passed to the page component as props
    }
}

export default function AddAdmin(props){

    const ctx = useAppContext()
    const I18n = ctx.I18n
    const axios = ctx.axios
    const router = useRouter()

    const [details, setDetails] = useState(router.query.query || '')
    const [email, setEmail] = useState('')
    const [userList, setUserList] = useState(props.users)
    const [pagination, setPagination] = usePagination()

    const [effectTrigger, triggerEffect] = useTriggerEffect()

    useEffect(() => {
        // alert(props.users[0].nameOrEmail)
        const params = {...router.query, admins: true}

        axios.get('users', {params}).then(res => {
            setPagination(res)
            const admins = res.data.map(u => {
                delete u['newsStats']
                return userMapper(u)
            })
            setUserList(admins)
        })
        // setUserList(props.users)

    }, [router.query, effectTrigger])

    const search = (e) => {
        if (e.key === 'Enter') {
            router.push({
                pathname: router.pathname,
                query: {...(e.target.value && {query: e.target.value})}
            })
        }
    }

    const handleChange = (e) => {
        setDetails(e.target.value)
    }

    const addAdmin = async () => {
        const res = await axios.put(`users/${props.id}/role`, undefined,{
            headers: {
                'Content-Type': 'application/json',
            },
            params: {role: 'ROLE_ADMIN'}
        })
        triggerEffect()
    }

    // return <></>

    return (
        <>
            <Head>
                <title>{I18n("moderation.panel")}</title>
            </Head>
            <div className="d-flex h-100 flex-column">
                <Modal onClickHandler={addAdmin} acceptText={I18n("tooltip.addAdmin")} id="addAdminModal" title={I18n("tooltip.addAdmin")}>
                    <div id="form-login-index" >
                        <div className="d-flex flex-column align-items-center">
                            <div className="d-flex align-items-center">
                                <img className="size-img-modal-login align-self-center"
                                     src="/img/profile-svgrepo-com.svg" alt="..."/>
                                <label htmlFor="email-input" className="sr-only">
                                    {I18n("login.mail.address")}
                                </label>
                                <input onChange={(e) => setEmail(e.target.value)} value={email} className="form-control text-white w-100 mb-2" id="email-input"
                                            placeholder={I18n("login.mail.address")}/>

                            </div>
                            {/*<div className="my-1">*/}
                            {/*    <form:errors cssClass="text-danger" path="email" element="small"/>*/}
                            {/*</div>*/}


                            {/*<button onClick={addAdmin} className="btn btn-info" type="submit">*/}
                            {/*    {I18n("tooltip.addAdmin")}*/}
                            {/*</button>*/}
                        </div>
                    </div>
                </Modal>
                <div className="flex-grow-1 d-flex flex-row">

                    <ModerationPanel/>
                    <div className="d-flex flex-column w-75 align-items-center">
                        <div className="w-100 my-3 d-flex flex-row justify-content-center">
                            <div className=" d-flex w-100 m-2 my-lg-0 ">
                                <div className="d-flex w-100 justify-content-center">
                                    <input onKeyDown={search} value={details} id="searchBar_addAdmin" style={{backgroundImage: "url(/img/loupe-svgrepo-com.svg)"}} className="search-form search form-control text-white w-55"
                                           type="search" placeholder={i18n("moderation.searchAdmin")} onChange={handleChange}/>
                                </div>
                            </div>

                            <div data-toggle="tooltip" data-placement="bottom" title={i18n("tooltip.addAdmin")}>
                                <button data-toggle="modal" data-target="#addAdminModal"
                                        className="mr-5 mt-1 add-admin-btn bg-transparent border-color-transparent"
                                        style={{backgroundImage: "url(/img/plus-svgrepo-com.svg)"}} >
                                </button>
                            </div>

                            {/*TODO: modal*/}
                        </div>
                        <div className="container-fluid">
                            <MainCardsContainer rows={3}>
                                {userList.map(c => <Creator admin triggerEffect={triggerEffect} key={`creator${c.id}`} {...c}></Creator>)}
                            </MainCardsContainer>

                        </div>
                    </div>

                </div>
                <Pagination {...pagination}></Pagination>

            </div>
        </>
    )
}