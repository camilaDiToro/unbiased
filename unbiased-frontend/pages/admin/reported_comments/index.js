import Moderation_panel from "../../../components/moderation_panel";

export default function Reported_comments(){

    return(
        <div className="d-flex h-100 flex-column">
            <div className="flex-grow-1 d-flex flex-row">
                <Moderation_panel/>
                hola soy reported comments
            </div>
        </div>
    )
}