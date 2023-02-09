import Tooltip from "./Tooltip";
import {useAppContext} from "../context";
import types from "../types";

export default function UserPrivileges(props) {
    const {I18n} = useAppContext()
    return <>
        <h6>
            {I18n("profile.modal.infoAllowedMsg")}
        </h6>

        <div className="info-function d-flex flex-row mb-3">

            <div className="d-flex">
                1. {I18n("profile.modal.infoChangeUsername")}
            </div>

            <div className="d-flex info-enabled info-custom-box">
                {I18n("profile.modal.enabled")}
            </div>
        </div>

        <div className="info-function d-flex flex-row mb-3">

            <div className="d-flex">
                2. {I18n("profile.modal.infoChangeProfileimg")}
            </div>

            <div className="d-flex info-enabled info-custom-box">
                {I18n("profile.modal.enabled")}
            </div>
        </div>

        <div className="info-function d-flex flex-row">

            <div className="d-flex">
                3. {I18n("profile.modal.infoChangeAddDescription")}
            </div>
            {props.isJournalist ? <div className="d-flex info-enabled info-custom-box">
                {I18n("profile.modal.enabled")}
            </div> : <Tooltip  position="bottom"
                              text={I18n("tooltip.infoDisabled")}>
               <div className="d-flex info-disabled info-custom-box">
                   {                   I18n("profile.modal.disabled")
                   }
               </div>
            </Tooltip>}
        </div></>
}

UserPrivileges.propTypes = types.UserPrivileges