import {useAppContext} from "../context";
import {Tooltip } from "@mui/material";
import {getResourcePath} from "../constants";
import types from "../types";

export default function PositivityIndicator(props) {
    const ctx = useAppContext()
    const tooltipText = `${ctx.I18n("home.upvotes", [parseInt(props.upvoted*100)])} - ${ctx.I18n("home.interactions", [props.interactions])}`
    const imgMap = {
        positive: "/img/looking-positivity.svg",
        controversial: "/img/controversial-positivity.svg",
        negative: "/img/angry-positivity.svg"
    }





    return <Tooltip title={tooltipText}>
        <img src={getResourcePath(imgMap[props.positivity || 'positive'])} alt="..."
             className={`quality-indicator${props.showNews ? '-news-view' : ''}`}
             data-original-title={tooltipText} />
    </Tooltip>
}

PositivityIndicator.propTypes = types.PositivityIndicator