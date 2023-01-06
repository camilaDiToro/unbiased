import Link from "next/link";
import {useCallback, useMemo, useState} from "react";


import dynamic from "next/dynamic";
import {useAppContext} from "../../context";
import Tooltip from "../../components/Tooltip";
import ModalTrigger from "../../components/ModalTrigger";
import BackButton from "../../components/BackButton";
import Modal from "../../components/Modal";


const MarkdownEditor = dynamic(
    () => import("react-simplemde-editor").then((mod) => mod.default),
    { ssr: false }
);




export default function CreateArticle(props) {
    const [value, setValue] = useState("Initial");
    const {I18n} = useAppContext()

    return <>
        {/*<link rel="stylesheet" href="https://cdn.rawgit.com/xcatliu/simplemde-theme-dark/master/dist/simplemde-theme-dark.min.css"/>*/}

        <ModalTrigger modalId="closeModal">
        <BackButton></BackButton>
        </ModalTrigger>
        <div className="d-flex flex-col align-items-center justify-content-center p-5">


            <form id="custom-form-group" className="h-auto w-50" action="/paw-2022b-6/create_article" method="post"
                  encType="multipart/form-data">

                {/*<div>*/}
                {/*    <label htmlFor="title">Title</label>*/}
                {/*    <div className="form-group">*/}
                {/*        <div className="input-group mb-3">*/}

                {/*            <input id="title" name="title" className="form-control is-invalid"*/}
                {/*                   placeholder="This is a title example" type="text" value=""/>*/}
                {/*                <p id="title.errors" className="invalid-feedback">The field cannot be empty</p>*/}

                {/*        </div>*/}
                {/*    </div>*/}
                {/*</div>*/}


                {/*<div>*/}
                {/*    <label htmlFor="subtitle">Description</label>*/}
                {/*    <div className="form-group">*/}
                {/*        <div className="input-group mb-3">*/}

                {/*            <input id="subtitle" name="subtitle" className="form-control is-invalid"*/}
                {/*                   placeholder="This is a description example" type="text" value=""/>*/}
                {/*                <p id="subtitle.errors" className="invalid-feedback">The field cannot be empty</p>*/}
                {/*        </div>*/}
                {/*    </div>*/}
                {/*</div>*/}


                <div>
                    <label htmlFor="body">Body</label>
                    <div className="form-group" data-color-mode="dark">
                        {/*<textarea id="body-text" name="body" className="form-control is-invalid"*/}
                        {/*          style={{display: 'none'}}></textarea>*/}
                        <div className="wmde-markdown-var"> </div>
                        <MarkdownEditor value={value} onChange={setValue}/>
                        <p id="body.errors" className="invalid-feedback">The field cannot be empty</p>
                    </div>

                </div>

                {/*<label htmlFor="image">Image of the notice </label>*/}
                {/*<div className="input-group mb-3">*/}
                {/*    <div className="custom-file">*/}
                {/*        <input id="fileInput" name="image" className="custom-file-input is-valid" type="file"*/}
                {/*               accept="image/png, image/jpeg"*/}
                {/*               value="org.springframework.web.multipart.commons.CommonsMultipartFile@684117c3"/>*/}
                {/*            <label htmlFor="inputGroupFile01" className="custom-file-label custom-file-input-label">Choose*/}
                {/*                file</label>*/}
                {/*    </div>*/}
                {/*</div>*/}

                <div className="dropdown" id="categories-dropdown">
                    <button className="btn btn-primary dropdown-toggle" type="button" id="dropdownMenuButton"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        CHOOOOOSE
                    </button>
                    <div className="dropdown-menu bg-dropdown" aria-labelledby="dropdownMenuButton">

                        <div className="form-check  w-100">
                            <input id="categories.tourism" name="categories" type="checkbox" value="categories.tourism"/><input
                                type="hidden" name="_categories" value="on"/>
                                <label className="form-check-label" htmlFor="categories.tourism">
                                    Tourism
                                </label>
                        </div>

                        <div className="form-check  w-100">
                            <input id="categories.entertainment" name="categories" type="checkbox"
                                   value="categories.entertainment"/><input type="hidden" name="_categories" value="on"/>
                                <label className="form-check-label" htmlFor="categories.entertainment">
                                    Entertainment
                                </label>
                        </div>

                        <div className="form-check  w-100">
                            <input id="categories.politics" name="categories" type="checkbox"
                                   value="categories.politics"/><input type="hidden" name="_categories" value="on"/>
                                <label className="form-check-label" htmlFor="categories.politics">
                                    Politics
                                </label>
                        </div>

                        <div className="form-check  w-100">
                            <input id="categories.economics" name="categories" type="checkbox"
                                   value="categories.economics"/><input type="hidden" name="_categories" value="on"/>
                                <label className="form-check-label" htmlFor="categories.economics">
                                    Economics
                                </label>
                        </div>

                        <div className="form-check  w-100">
                            <input id="categories.sports" name="categories" type="checkbox"
                                   value="categories.sports"/><input type="hidden" name="_categories" value="on"/>
                                <label className="form-check-label" htmlFor="categories.sports">
                                    Sports
                                </label>
                        </div>

                        <div className="form-check  w-100">
                            <input id="categories.technology" name="categories" type="checkbox"
                                   value="categories.technology"/><input type="hidden" name="_categories" value="on"/>
                                <label className="form-check-label" htmlFor="categories.technology">
                                    Technology
                                </label>
                        </div>

                        <div className="form-check  w-100">
                            <input id="categories.forMe" name="categories" type="checkbox"
                                   value="categories.forMe"/><input type="hidden" name="_categories" value="on"/>
                                <label className="form-check-label" htmlFor="categories.forMe">
                                    For me
                                </label>
                        </div>

                        <div className="form-check  w-100">
                            <input id="categories.all" name="categories" type="checkbox" value="categories.all"/><input
                                type="hidden" name="_categories" value="on"/>
                                <label className="form-check-label" htmlFor="categories.all">
                                    All
                                </label>
                        </div>


                    </div>
                </div>

                <div className="w-100 d-flex justify-content-end">
                    <button className="btn btn-info" type="submit">Saveee</button>
                </div>
            </form>

            <Modal id="closeModal" body={I18n("createArticle.modal.msg")} title="createArticle.modal.question"></Modal>

    </div>
    </>
}