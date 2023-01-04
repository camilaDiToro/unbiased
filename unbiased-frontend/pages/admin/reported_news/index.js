import Moderation_panel from "../../../components/moderation_panel";

export default function reported_news(){

    return (
        <div className="d-flex h-100 flex-column">
            {/*LEFT-SIDE*/}
            <div className="flex-grow-1 d-flex flex-row">

                <Moderation_panel/>

                <div className="d-flex flex-column w-75">
                    hola soy reported news
                </div>

            </div>
        </div>
    )
}