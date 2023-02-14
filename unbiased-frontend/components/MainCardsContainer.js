import types from "../types";
import React from "react"
import {useAppContext} from "../context";

export default function MainCardsContainer(props) {
    const {I18n} = useAppContext()
    const noChildren = () => {
       return !React.Children.count(props.children)
    }

    if (!props.loaded)
        return <></>

    return <div className="container-fluid">
        {noChildren() ?

            <div className="h-75 d-flex flex-column justify-content-center align-items-center flex-grow-1 mt-5">
                <h2 className="fw-normal">
                    {I18n("home.emptyCategory.sorry")}
                </h2>
                <p className="lead">
                    {props.comments ? I18n("moderation.emptyComments") : (props.admin ? I18n("moderation.emptyAdmins"):  I18n("moderation.emptyArticles"))}
                </p>
            </div>
            :             <div className={`row row-cols-md-${props.rows} ${props.reported ? 'px-4' : ''}`}>
                {props.children} </div>}


    </div>
}

MainCardsContainer.propTypes = types.MainCardsContainer