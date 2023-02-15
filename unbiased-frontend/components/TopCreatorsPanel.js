import {useAppContext} from "../context";
import TopCreator from "./TopCreator";
import {getResourcePath} from "../constants";
import types from "../types";

export default function TopCreatorsPanel(props) {
    const {I18n} = useAppContext()
    return <div
        className="card container w-100 w-xl-25 p-4 h-auto m-2 h-fit align-self-xl-start"
        id="none_shadow"
    >
        <h5
            style={{ backgroundImage: `url('${getResourcePath('/img/crown-svgrepo-com.svg')}')` }}
            className="card-title top-creators"
        >
            {I18n("home.topCreators")}
        </h5>

        {props.creators.length === 0 ? (
            <h6 data-testid="no-creators" className="text-info m-1">{I18n("home.emptyCreators")}</h6>
        ) : (
            <></>
        )}
        {props.creators}
    </div>
}

TopCreatorsPanel.propTypes = types.TopCreatorsPanel