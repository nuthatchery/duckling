module Plugin

import util::IDE;
import ParseTree;
import IO;
import org::ducklinglang::duckling::frontend::Syntax;

void main() {
   registerLanguage("ExLang", "ex", Tree(str src, loc l) {
     pt = parse(#start[CompilationUnit], src, l);
     return pt;
   });
}